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
            <textarea :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input" rows="6"></textarea>
          </div>
        </template>

        <template v-if="field.type.code === 'RICHTEXT'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input">
          </div>
        </template>

        <template v-if="field.type.code === 'UID'">
          <div :class="{required: !field.nullable}" class="form-field w-full">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <div class="flex flex-wrap items-stretch w-full">
              <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input flex-shrink flex-grow flex-auto border-r-0 rounded-r-none">
              <div class="flex -mr-px">
                <button class="p-btn--primary rounded-l-none" type="button" @click="$set(item, field.dbFieldName, $utils.generateUUID())">
                  <i class="fas fa-sync"></i>
                </button>
              </div>
            </div>
          </div>
        </template>

        <template v-if="field.type.code === 'DATE'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input" type="date">
          </div>
        </template>

        <template v-if="field.type.code === 'TIME'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input" type="time">
          </div>
        </template>

        <template v-if="field.type.code === 'DATETIME'">
          <div :class="{required: !field.nullable}" class="form-field">
            <label :for="`input-` + field.dbFieldName">{{ field.name }}</label>
            <input :id="`input-` + field.dbFieldName" v-model="item[field.dbFieldName]" :name="field.dbFieldName" :required="!field.nullable" class="form-input" type="datetime-local">
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
    switch (this.mode) {
      case 'add':
        await this.$axios.$post(`/${ this.content.slug }`, this.item)
        break
      case 'edit':
        await this.$axios.$put(`/${ this.content.slug }/${ this.item.id }`, this.item)
        break
      default:
        break
    }
    await this.$router.push(`/contents/${ this.content.id }`)
  }
}
</script>
