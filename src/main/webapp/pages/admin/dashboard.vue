<template>
  <section>
    <h3 class="app-title">Admin dashboard</h3>

    <div class="grid grid-cols-1 md:grid-cols-2 items-stretch gap-2">

      <div class="dashboard-card">
        <h5 class="dashboard-card-title">Health</h5>

        <div class="description-container">
          <template v-for="(component, componentName, componentIndex) in health.components">
            <div :key="componentIndex" class="description-item">
              <div class="description-item-right font-bold">
                <span>{{ componentName }}</span>
              </div>
              <span :class="getStatusClass(component.status)" class="font-bold">{{ component.status }}</span>
            </div>
            <div v-for="(details, detailsName, detailsIndex) in component.details" :key="detailsIndex + ' ' + componentIndex" class="description-item">
              <div class="description-item-right pl-6">
                <span>{{ detailsName }}</span>
              </div>
              <span v-if="componentName === 'diskSpace' && typeof details === 'number'">{{ $utils.formatBytes(details) }}</span>
              <pre v-else-if="typeof details === 'object'">{{ details }}</pre>
              <span v-else>{{ details }}</span>
            </div>
          </template>
        </div>
      </div>

      <div class="dashboard-card">
        <h5 class="dashboard-card-title">Threads</h5>
        <dashboard-line :datasets="datasetThread" :items="threads" label-attribute="timestamp"/>
      </div>

      <div class="dashboard-card">
        <h5 class="dashboard-card-title">JVM</h5>
        <dashboard-line :datasets="datasetJvm" :items="jvmInfos" label-attribute="timestamp"/>
      </div>

      <div class="dashboard-card">
        <h5 class="dashboard-card-title">JVM - Non Heap</h5>
        <dashboard-line :datasets="datasetJvm" :items="jvmNonHeapInfos" label-attribute="timestamp"/>
      </div>

      <div class="dashboard-card">
        <h5 class="dashboard-card-title">CPU Usage</h5>
        <dashboard-line :datasets="datasetCpuUsage" :items="cpuUsage" :options="{ yAxes: { suggestedMax: 100 }}" label-attribute="timestamp"/>
      </div>
    </div>
  </section>
</template>

<script lang="ts">
import { Action, Component, State, Vue } from 'nuxt-property-decorator'
import DashboardLine from '~/components/shared/DashboardLine.vue'

@Component({
  components: {
    DashboardLine,
  },
})
export default class PageAdminDashboard extends Vue {

  @State(state => state.actuator.cpuUsage) cpuUsage
  @State(state => state.actuator.threads) threads
  @State(state => state.actuator.jvmInfos) jvmInfos
  @State(state => state.actuator.jvmNonHeapInfos) jvmNonHeapInfos

  @Action('actuator/setCpuUsage') setCpuUsage
  @Action('actuator/setThreads') setThreads
  @Action('actuator/setJvmInfos') setJvmInfos
  @Action('actuator/setJvmNonHeapInfos') setJvmNonHeapInfos

  datasetCpuUsage = [
    { data: (item) => item.used, label: 'USED', color: '#717eec' },
  ]

  datasetThread = [
    { data: (item) => item.deamon, label: 'WAITING', color: '#717eec' },
    { data: (item) => item.live, label: 'LIVE', color: '#ffeb3b' },
  ]

  datasetJvm = [
    { data: (item) => item.used / 1000000, label: 'USED (MB)', color: '#717eec' },
    { data: (item) => item.size / 1000000, label: 'SIZE (MB)', color: '#ffeb3b' },
  ]

  health = {}

  async asyncData (ctx) {
    const res_health = await ctx.$axios.$get('/actuator/health')

    await ctx.store.dispatch('actuator/setCpuUsage')
    await ctx.store.dispatch('actuator/setThreads')
    await ctx.store.dispatch('actuator/setJvmInfos')
    await ctx.store.dispatch('actuator/setJvmNonHeapInfos')

    return {
      health: res_health,
    }
  }

  async fetch () {
    this.$socket.client.subscribe('/topic/metrics', async (message) => {
      await this.setCpuUsage()
      await this.setThreads()
      await this.setJvmInfos()
      await this.setJvmNonHeapInfos()
    }, { id: 'sub-metrics' })
  }

  destroyed () {
    if (this.$socket.client.connected) {
      this.$socket.client.unsubscribe('sub-metrics')
    }
  }

  getStatusClass (status) {
    if (status === 'DOWN') {
      return 'text-danger-500'
    }
    return 'text-success-500'
  }

}
</script>

<style lang="scss" scoped>
.dashboard-card {
  @apply border rounded bg-white p-2;
}

.dashboard-card-title {
  @apply text-xl font-semibold mb-6;
}

.description-container {
  @apply px-4 -mt-2 divide-y divide-gray-200;
}

.description-item {
  @apply flex items-center justify-between py-1 text-sm;
}

.description-item-right {
  @apply flex items-center space-x-2 text-gray-700;
}
</style>
