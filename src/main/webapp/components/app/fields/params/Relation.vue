<template>
  <section class="grid grid-cols-2 gap-2">
    <fieldset class="form-field">
      <select v-model="field.params.type" class="form-input">
        <option v-for="(relationType, relationTypeIndex) in fieldRelationTypes" :key="relationTypeIndex" :value="relationType.code">{{ relationType.label }}</option>
      </select>
    </fieldset>
  </section>
</template>

<script lang="ts">
import { Action, Component, mixins, State } from 'nuxt-property-decorator'
import { FieldParamsMixin } from '~/mixins/field-params.mixin'

@Component
export default class RelationParam extends mixins(FieldParamsMixin) {
  @State(state => state.lists.fieldRelationTypes) fieldRelationTypes

  @Action('lists/findFieldRelationTypes') findFieldRelationTypes

  async fetch () {
    await this.findFieldRelationTypes()
  }
}
</script>
