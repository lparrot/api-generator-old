<template>
  <div v-if="!$fetchState.pending">
    <h3 class="text-gray-700 text-3xl font-medium">{{ mode === 'add' ? `Adding new ${ content.name }` : `Edit ${ content.name }` }}</h3>

    <form @submit.prevent="submit">
      <div v-for="(field, fieldIndex) in fields" :key="fieldIndex">
        <template v-if="field.type.code === 'STRING'">
          <label>
            <span>{{ field.name }}</span>
            <input v-model="contentData[field.dbFieldName]" :name="field.dbFieldName">
          </label>
        </template>

        <template v-if="field.type.code === 'TEXT'">
          <label>
            <span>{{ field.name }}</span>
            <input v-model="contentData[field.dbFieldName]" :name="field.dbFieldName">
          </label>
        </template>

        <template v-if="field.type.code === 'RICHTEXT'">
          <label>
            <span>{{ field.name }}</span>
            <input v-model="contentData[field.dbFieldName]" :name="field.dbFieldName">
          </label>
        </template>

        <template v-if="field.type.code === 'UID'">
          <label>
            <span>{{ field.name }}</span>
            <input v-model="contentData[field.dbFieldName]" :name="field.dbFieldName">
          </label>
        </template>

        <template v-if="field.type.code === 'DATE'">
          <label>
            <span>{{ field.name }}</span>
            <input v-model="contentData[field.dbFieldName]" :name="field.dbFieldName">
          </label>
        </template>

        <template v-if="field.type.code === 'TIME'">
          <label>
            <span>{{ field.name }}</span>
            <input v-model="contentData[field.dbFieldName]" :name="field.dbFieldName">
          </label>
        </template>

        <template v-if="field.type.code === 'DATETIME'">
          <label>
            <span>{{ field.name }}</span>
            <input v-model="contentData[field.dbFieldName]" :name="field.dbFieldName">
          </label>
        </template>

        <template v-if="field.type.code === 'RELATION'">
          <label>
            <span>{{ field.name }}</span>
            <input v-model="contentData[field.dbFieldName]" :name="field.dbFieldName">
          </label>
        </template>
      </div>

      <button class="p-btn--success" type="submit">{{ mode === 'add' ? 'Create' : 'Update' }}</button>
    </form>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'

declare type EditMode = 'add' | 'edit'

@Component
export default class PageIndex extends Vue {
  content: any = {}
  contentData: any = {}

  get fields () {
    return this.content.contentFields
      .filter(field => !field.primaryKey)
  }

  get mode (): EditMode {
    return this.$route.params.idData != null ? 'edit' : 'add'
  }

  async fetch () {
    const idContent = this.$route.params.idContent
    this.content = await this.$axios.$get(`/contents/${ idContent }`)
    if (this.mode === 'edit') {
      this.contentData = await this.$axios.$get(`/${ this.content.slug }/${ this.$route.params.idData }`)
    }
  }

  async submit () {
    await this.$axios.$post(`/${ this.content.slug }/v1`, this.contentData)
  }
}
</script>
