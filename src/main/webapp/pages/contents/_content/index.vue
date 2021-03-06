<template>
  <div v-if="!$fetchState.pending">
    <h3 class="app-title">Content of {{ content.name }} table</h3>

    <div class="flex flex-col md:flex-row gap-2 my-4">
      <router-link :to="`/contents/${content.id}/edit`" class="p-btn--primary">Add new item</router-link>
      <router-link :to="`/content-types/${content.id}`" class="p-btn--success">Edit content structure</router-link>
      <button class="p-btn--danger" @click="deleteContent">Delete content {{ content.name }}</button>
    </div>

    <datatable :fields="fieldForDatatable" :items="list" :pagination.sync="pagination" with-actions @context-changed="onDatatableContextChanged">
      <template v-for="(field, fieldIndex) in fieldForDatatable" v-slot:[getCellSlot(field.key)]="{item}">
        <span v-if="field.type === 'PROPERTY'" :key="fieldIndex">{{ $utils.get(item, field.key) }}</span>
        <span v-else-if="field.type === 'EXPRESSION'" :key="fieldIndex">{{ $expLang.convert(item, field.key) }}</span>
      </template>

      <template #cell(actions)="{item}">
        <div class="flex">
          <router-link :to="`/contents/${content.id}/edit/${item.id}`" class="p-btn--primary block rounded-full px-2 py-1.5 mx-1">
            <i class="fas fa-pencil-alt"></i>
          </router-link>
          <button class="p-btn--danger block rounded-full px-2 py-1.5 mx-1" @click="deleteData(item)">
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
  pagination: any = { page: 1, size: 5, total: 0 }

  get fields () {
    return this.content.contentFields
      .filter(field => !field.hideInList)
  }

  get fieldForDatatable () {
    if (this.content.contentShowFields != null && this.content.contentShowFields.length > 0) {
      return this.content.contentShowFields
    }

    return this.fields
      .map(field => {
        return { key: field.dbFieldName, label: field.name }
      })
  }

  async fetch () {
    try {
      this.content = await this.$axios.$get(`/contents/${ this.$route.params.content }`)
      await this.search()
    } catch (err) {
      if (err?.response?.status === 404) {
        return this.$nuxt.error({ statusCode: 404, path: '404', message: 'This page could not be found' })
      }
      throw err
    }
  }

  getCellSlot (key) {
    return `cell(${ key })`
  }

  async search () {
    const res = await this.$axios.$get(`/data/${ this.content.slug }`, { params: { size: this.pagination.size, page: this.pagination.page } })
    this.list = res.list
    this.pagination.total = res.total
  }

  async deleteData (item) {
    await this.$axios.$delete(`/data/${ this.content.slug }/${ item.id }`)
    await this.search()
  }

  async deleteContent () {
    await this.$axios.$delete(`/contents/${ this.content.id }`)
    await this.$router.push(`/content-types`)
    await this.findContents()
  }

  async onDatatableContextChanged (pagination) {
    await this.search()
  }
}
</script>
