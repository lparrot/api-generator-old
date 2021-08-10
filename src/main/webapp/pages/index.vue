<template>
  <div>
    <h3 class="app-title">Blank Page</h3>

    <div class="flex gap-2">
      <button class="p-btn--success" @click="exportContent">Exporter</button>

      <upload v-model="selectedFile" #default="{showUpload, deleteFile}" @input="importContent">
        <div class="w-full flex flex-col gap-2">
          <div class="items-center bg-grey-lighter-500 px-4 py-2 bg-white text-blue-500 rounded tracking-wide uppercase border border-blue-500 cursor-pointer hover:bg-blue-500 hover:text-white" @click="showUpload">
            <i class="fas fa-cloud-upload-alt"></i>
            <span class="text-base leading-normal">Import json file</span>
          </div>
          <div v-if="selectedFile != null" class="flex gap-2 align-middle bg-grey-lighter px-4 py-2 bg-white text-blue rounded-lg tracking-wide uppercase border border-blue">
            <div>{{ selectedFile.name }} {{ $utils.formatBytes(selectedFile.size) }}</div>
          </div>
        </div>
      </upload>
    </div>
  </div>
</template>

<script lang="ts">
import { Action, Component, Vue } from 'nuxt-property-decorator'
import Upload from '~/components/shared/Upload.vue'

@Component({
  components: {
    Upload,
  },
})
export default class PageIndex extends Vue {

  @Action('findContents') findContents

  selectedFile: any = null

  async exportContent () {
    const res_export_contents = await this.$axios.$get('/exports/contents?full=true')
    this.$utils.downloadFile(res_export_contents, 'export_content.json')
  }

  async importContent (file: File) {
    const formData = new FormData()
    formData.append('file', file)
    await this.$axios.$post('/imports/contents', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
    this.selectedFile = null
    await this.findContents()
  }

}
</script>
