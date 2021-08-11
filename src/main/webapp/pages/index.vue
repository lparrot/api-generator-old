<template>
  <div>
    <h3 class="app-title">Blank Page</h3>

    <div class="flex gap-2">
      <button class="p-btn--success" @click="exportContent">
        <i class="fas fa-file-export"></i>
        <span>Exporter</span>
      </button>

      <upload v-model="selectedFile" #default="{showUpload, deleteFile}" @input="importContent">
        <div class="w-full flex flex-col gap-2">
          <button class="p-btn--success" type="button" @click="showUpload">
            <i class="fas fa-cloud-upload-alt"></i>
            <span>Importer</span>
          </button>
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
