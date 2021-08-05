<template>
  <section>
    <h3 class="app-title">Tasks</h3>

    <table class="w-full table-auto">
      <thead>
        <tr>
          <th class="text-left">Timestamp</th>
          <th class="text-left">Method</th>
          <th class="text-left">Status</th>
          <th class="text-left">Time</th>
          <th class="text-left">Path</th>
          <th class="text-left"></th>
          <th class="text-left"></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(trace, traceIndex) in traces" :key="traceIndex">
          <td class="">
            <div>{{ new Date(trace.timestamp).toDateString() }}</div>
          </td>
          <td>
            <div :class="getMethodColor(trace.request.method)">{{ trace.request.method }}</div>
          </td>
          <td>
            <div :class="getStatusColor(trace.response.status)" class="px-2 py-1 rounded text-white text-xs">{{ trace.response.status }}</div>
          </td>
          <td>
            {{ trace.timeTaken }} ms
          </td>
          <td>
            <div class="text-lg text-primary-500 font-thin">{{ trace.request.uri }}</div>
          </td>
          <td></td>
          <td></td>
        </tr>
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
