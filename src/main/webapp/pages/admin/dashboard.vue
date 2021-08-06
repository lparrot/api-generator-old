<template>
  <section v-if="!$fetchState.pending">
    <h3 class="app-title">Admin dashboard</h3>

    <div class="grid grid-cols-1 md:grid-cols-2 items-stretch gap-2">

      <div class="dashboard-card">
        <h5 class="dashboard-card-title">Health</h5>

        <div class="description-container">
          <div class="description-item">
            <div class="description-item-right font-bold">
              <span>Instance</span>
            </div>
            <span :class="getStatusClass(health.components.ping.status)">{{ health.components.ping.status }}</span>
          </div>
          <div class="description-item">
            <div class="description-item-right font-bold">
              <span>Database</span>
            </div>
            <span :class="getStatusClass(health.components.db.status)">{{ health.components.db.status }}</span>
          </div>
          <div class="description-item">
            <div class="description-item-right pl-6">
              <span>Vendor</span>
            </div>
            <span>{{ health.components.db.details.database }}</span>
          </div>
          <div class="description-item">
            <div class="description-item-right font-bold">
              <span>Disk space</span>
            </div>
            <span :class="getStatusClass(health.components.diskSpace.status)">{{ health.components.diskSpace.status }}</span>
          </div>
          <div class="description-item">
            <div class="description-item-right pl-6">
              <span>Total</span>
            </div>
            <span>{{ $utils.formatBytes(health.components.diskSpace.details.total) }}</span>
          </div>
          <div class="description-item">
            <div class="description-item-right pl-6">
              <span>Free</span>
            </div>
            <span>{{ $utils.formatBytes(health.components.diskSpace.details.free) }}</span>
          </div>
          <div class="description-item">
            <div class="description-item-right pl-6">
              <span>Threshold</span>
            </div>
            <span>{{ $utils.formatBytes(health.components.diskSpace.details.threshold) }}</span>
          </div>
        </div>
      </div>

      <div class="dashboard-card">
        <h5 class="dashboard-card-title">Health</h5>

        <button class="p-btn--success w-full" @click="setThreads">Refresh</button>
      </div>
      <div class="dashboard-card">
        <h5 class="dashboard-card-title">Health</h5>
      </div>
    </div>
  </section>
</template>

<script lang="ts">
import { Action, Component, Vue } from 'nuxt-property-decorator'

@Component
export default class PageAdminDashboard extends Vue {
  @Action('actuator/setThreads') setThreads

  health = {}

  async fetch () {
    this.health = await this.$axios.$get('/actuator/health')
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
