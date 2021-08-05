<template>
  <section>
    <h3 class="app-title">Settings</h3>

    <button class="p-btn--success" @click="restartApplication">Restart application</button>
  </section>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'

@Component
export default class PageAdminSettings extends Vue {
  async restartApplication () {
    await this.$axios.$post('/app/restart')
  }
}
</script>
