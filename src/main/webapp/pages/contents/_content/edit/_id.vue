<template>
  <div v-if="!$fetchState.pending">
    <h3 class="text-gray-700 text-3xl font-medium">{{ mode === 'add' ? `Adding new ${ content.name }` : `Edit ${ content.name }` }}</h3>

    <form @submit.prevent="submit">
      <div v-for="(field, fieldIndex) in fields" :key="fieldIndex">
        <template v-if="field.type.code === 'STRING'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input">
          </div>
        </template>

        <template v-if="field.type.code === 'NUMBER'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input" type="number">
          </div>
        </template>

        <template v-if="field.type.code === 'TEXT'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input">
          </div>
        </template>

        <template v-if="field.type.code === 'RICHTEXT'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input">
          </div>
        </template>

        <template v-if="field.type.code === 'UID'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input">
          </div>
        </template>

        <template v-if="field.type.code === 'DATE'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input">
          </div>
        </template>

        <template v-if="field.type.code === 'TIME'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input">
          </div>
        </template>

        <template v-if="field.type.code === 'DATETIME'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input">
          </div>
        </template>

        <template v-if="field.type.code === 'RELATION'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input">
          </div>
        </template>
      </div>

      <button class="p-btn--success mt-2" type="submit">{{ mode === 'add' ? 'Create' : 'Update' }}</button>
    </form>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'

declare type EditMode = 'add' | 'edit'

@Component
export default class PageIndex extends Vue {
  content: any = {}
  item: any = {}

  get fields () {
    return this.content.contentFields
      .filter(field => !field.primaryKey)
  }

  get mode (): EditMode {
    return this.$route.params.id != null ? 'edit' : 'add'
  }

  async fetch () {
    this.content = await this.$axios.$get(`/contents/${ this.$route.params.content }`)
    if (this.mode === 'edit') {
      this.item = await this.$axios.$get(`/${ this.content.slug }/${ this.$route.params.id }`)
    }
  }

  async submit () {
    await this.$axios.$post(`/${ this.content.slug }`, this.item)
  }
}
</script>
