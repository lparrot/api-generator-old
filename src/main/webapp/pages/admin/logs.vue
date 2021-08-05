<template>
  <section>
    <h3 class="app-title">Logs</h3>

    <button class="p-btn--success w-full mb-4" @click="openFile">Download file</button>

    <div class="bg-white rounded p-4 max-h-[40rem] overflow-auto">
      <pre class="text-xs">{{ logs }}</pre>
      <div ref="divBottom"></div>
    </div>
  </section>
</template>

<script lang="ts">
import { Component, Ref, Vue } from 'nuxt-property-decorator'

@Component
export default class PageAdminTasks extends Vue {
  @Ref('divBottom') divBottom

  logs = null

  async asyncData (ctx) {
    const logs = await ctx.$axios.$get('/actuator/logfile')
    return {
      logs,
    }
  }

  async mounted () {
    this.divBottom.scrollIntoView()
  }

  async openFile () {
    window.open('/api/actuator/logfile')
  }
}
</script>
