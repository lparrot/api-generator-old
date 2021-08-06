<template>
  <section>
    <h3 class="app-title">Tasks</h3>

    <table class="w-full table-auto">
      <thead class="border-b">
        <tr>
          <th class="text-left py-2">Timestamp</th>
          <th class="text-left">Method</th>
          <th class="text-left">Status</th>
          <th class="text-left">Time</th>
          <th class="text-left">Path</th>
          <th class="text-left">Principal</th>
        </tr>
      </thead>
      <tbody>
        <template v-for="(trace, traceIndex) in traces">
          <tr :key="traceIndex">
            <td>
              <span>{{ new Date(trace.timestamp).toDateString() }}</span>
            </td>
            <td>
              <span :class="getMethodColor(trace.request.method)">{{ trace.request.method }}</span>
            </td>
            <td>
              <span :class="getStatusColor(trace.response.status)" class="px-2 py-1 rounded text-white text-xs">{{ trace.response.status }}</span>
            </td>
            <td>
              <span>{{ trace.timeTaken }} ms</span>
            </td>
            <td>
              <span class="text-lg text-primary-500 font-thin">{{ trace.request.uri }}</span>
            </td>
            <td>
              <span>{{ trace.principal }}</span>
            </td>
            <td></td>
          </tr>
        </template>
      </tbody>
    </table>
  </section>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'

@Component
export default class PageAdminTasks extends Vue {
  traces = null

  async fetch () {
    const res = await this.$axios.$get('/actuator/httptrace')
    this.traces = res.traces
  }

  getMethodColor (method) {
    switch (method) {
      case 'GET':
        return 'text-info-500'
      case 'PUT':
      case 'POST':
        return 'text-success-500'
      case 'DELETE':
        return 'text-danger-500'
      default:
        return 'text-primary-500'
    }
  }

  getStatusColor (status) {
    const statusCode: string = String(status)
    if (statusCode.startsWith('1') || statusCode.startsWith('2')) {
      return 'bg-success-500'
    } else if (statusCode.startsWith('3')) {
      return 'bg-warning-500'
    } else {
      return 'bg-danger-500'
    }
  }
}
</script>
