<template>
  <div v-if="!$fetchState.pending">
    <h3 class="text-gray-700 text-3xl font-medium pb-4">Content of {{ content.name }} table</h3>

    <div class="my-4">
      <router-link :to="`/contents/${content.id}/edit`" class="p-btn--primary">Add new {{ content.name }}</router-link>
      <router-link :to="`/content-types/${content.id}`" class="p-btn--success ml-2">Edit content structure</router-link>
      <button class="p-btn--danger ml-2" @click="deleteContent">Delete content {{ content.name }}</button>
    </div>

    <datatable :fields="fieldForDatatable" :items="list" with-actions>
      <template #cell(actions)="{item}">
        <div class="flex">
          <router-link :to="`/contents/${content.id}/edit/${item.id}`" class="p-btn--primary block rounded-full px-2 py-1.5 mx-1" @>
            <i class="fas fa-pencil-alt"></i>
          </router-link>
          <button class="p-btn--danger block rounded-full px-2 py-1.5 mx-1">
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </template>
    </datatable>

  </div>
</template>

<script lang="ts">
import { Action, Component, Vue } from 'nuxt-property-decorator'
import Datatable from '~/components/shared/Datatable.vue'

@Component({
  components: {
    Datatable,
  },
})
export default class PageContentIndex extends Vue {
  @Action('findContents') findContents

  content: any = {}
  list: any[] = []

  get fields () {
    return this.content.contentFields
      .filter(field => !field.hideInList)
  }

  get fieldForDatatable () {
    return this.content.contentFields.filter(field => !field.hideInList).map(field => {
      return { key: field.dbFieldName, label: field.name }
    })
  }

  async fetch () {
    this.content = await this.$axios.$get(`/contents/${ this.$route.params.content }`)
    this.list = await this.$axios.$get(`/${ this.content.slug }`)
  }

  async deleteContent () {
    await this.$axios.$delete(`/contents/${ this.content.id }`)
    await this.$router.push(`/content-types`)
    await this.findContents()
  }
}
</script>
