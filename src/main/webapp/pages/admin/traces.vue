<template>
  <section>
    <h3 class="app-title">Tasks</h3>

    {{ traces }}
  </section>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'

@Component
export default class PageAdminTasks extends Vue {
  traces = null

  async fetch () {
    this.traces = await this.$axios.$get('/actuator/httptrace')
  }
}
</script>
