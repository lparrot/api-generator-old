<template>
  <section>
    <h3 class="app-title">Tasks</h3>

    {{ tasks }}
  </section>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'

@Component
export default class PageAdminTasks extends Vue {
  tasks = null

  async fetch () {
    this.tasks = await this.$axios.$get('/actuator/scheduledtasks')
  }
}
</script>
