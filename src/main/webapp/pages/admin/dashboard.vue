<template>
  <section v-if="!$fetchState.pending">
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
            <div v-for="(details, detailsName, detailsIndex) in component.details" :key="componentIndex" class="description-item">
              <div class="description-item-right pl-6">
                <span>{{ detailsName }}</span>
              </div>
              <span v-if="componentName === 'diskSpace' && typeof details === 'number'">{{ $utils.formatBytes(details) }}</span>
              <pre v-if="typeof details === 'object'">{{ details }}</pre>
              <span v-else>{{ details }}</span>
            </div>
          </template>
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
