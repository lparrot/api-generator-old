<template>
  <section class="grid grid-cols-2 gap-2">

  </section>
</template>

<script lang="ts">
import { Component, mixins } from 'nuxt-property-decorator'
import { FieldParamsMixin } from '~/mixins/field-params.mixin'

@Component
export default class TimeParam extends mixins(FieldParamsMixin) {

}
</script>
