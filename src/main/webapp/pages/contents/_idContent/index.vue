<template>
  <div v-if="!$fetchState.pending">
    <h3 class="text-gray-700 text-3xl font-medium pb-4">Content of {{ content.name }} table</h3>

    <div class="my-4">
      <router-link :to="`/contents/edit/${content.id}`" class="p-btn--primary">Add new {{ content.name }}</router-link>
    </div>

    <table class="min-w-full table-auto">
      <thead class="justify-between">
        <tr class="bg-gray-800">
          <th v-for="(field, fieldIndex) in fields" :key="fieldIndex" class="px-16 py-2">
            <span class="text-gray-300">{{ field.name }}</span>
          </th>
          <th class="px-16 py-2"></th>
        </tr>
      </thead>
      <tbody class="bg-gray-200">
        <tr v-for="(item, itemIndex) in list" :key="itemIndex" class="bg-white border-4 border-gray-200 py-4">
          <td v-for="(field, fieldIndex) in fields" :key="itemIndex + fieldIndex">
            <div class="text-center ml-2 font-semibold">
              {{ item[field.dbFieldName] }}
            </div>
          </td>
          <td class="py-4">
            <router-link :to="`/contents/edit/${content.id}/${item.id}`" class="p-btn--primary">Edit</router-link>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'

@Component
export default class PageContentIndex extends Vue {
  content: any = {}
  list: any[] = []

  get fields () {
    return this.content.contentFields
      .filter(field => !field.hideInList)
  }

  async fetch () {
    const idContent = this.$route.params.idContent
    this.content = await this.$axios.$get(`/contents/${ idContent }`)
    this.list = await this.$axios.$get(`/${ this.content.slug }`)
  }
}
</script>
