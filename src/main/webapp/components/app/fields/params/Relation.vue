<template>
  <section class="grid grid-cols-3 gap-2">
    <fieldset class="form-field required">
      <label for="select-type">Relation type</label>
      <select id="select-type" v-model="field.params.type" class="form-input">
        <option v-for="(relationType, relationTypeIndex) in fieldRelationTypes" :key="relationTypeIndex" :value="relationType.code">{{ relationType.label }}</option>
      </select>
    </fieldset>

    <fieldset class="form-field required">
      <label for="select-targetcontent">Targeted content</label>
      <select id="select-targetcontent" v-model="field.params.targetContent" class="form-input">
        <option v-for="(content, contentIndex) in contents" :key="contentIndex" :value="content.slug">{{ content.name }}</option>
      </select>
    </fieldset>

    <fieldset v-if="fieldList != null" class="form-field required">
      <label for="select-fieldlist">Targeted field</label>
      <select id="select-fieldlist" v-model="field.params.targetField" class="form-input">
        <option v-for="(field, fieldIndex) in fieldList" :key="fieldIndex" :value="field.dbFieldName">{{ field.name }}</option>
      </select>
    </fieldset>
  </section>
</template>

<script lang="ts">
import { Action, Component, mixins, State, Watch } from 'nuxt-property-decorator'
import { FieldParamsMixin } from '~/mixins/field-params.mixin'

@Component
export default class RelationParam extends mixins(FieldParamsMixin) {
  @State(state => state.lists.fieldRelationTypes) fieldRelationTypes
  @State(state => state.contents) contents

  @Action('lists/findFieldRelationTypes') findFieldRelationTypes

  fieldList = null

  async fetch () {
    await this.findFieldRelationTypes()
  }

  @Watch('field.params.targetContent', { immediate: true })
  onParamsTargetContentChange (value) {
    const content = this.contents.find(content => content.slug === value)
    console.log(content)
    if (content != null) {
      this.fieldList = content.contentFields
      console.log(this.fieldList)
    }
  }
}
</script>
