<template>
  <section>
    <h3 class="app-title">Journal</h3>

    <table class="w-full table-auto">
      <thead class="border-b">
        <tr>
          <th class="text-left py-2">Timestamp</th>
          <th class="text-left py-2">Type</th>
          <th class="text-left py-2">Data</th>
          <th class="text-left py-2">Principal</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(event, eventIndex) in events" :key="eventIndex" class="border-b align-top py-2">
          <td>
            <span>{{ new Date(event.timestamp).toLocaleDateString() + ' ' + new Date(event.timestamp).toLocaleTimeString() }}</span>
          </td>
          <td>
            <div>{{ event.type }}</div>
          </td>
          <td>
            <div v-for="(data, dataName, dataIndex) in event.data" :key="dataIndex" class="bg-primary-400 text-white px-1 rounded ml-4 w-max my-1">
              {{ dataName }} : {{ data }}
            </div>
          </td>
          <td>
            <span>{{ event.principal }}</span>
          </td>
        </tr>
      </tbody>
    </table>
  </section>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'
import { Context } from '@nuxt/types'

@Component
export default class PageAdminEvents extends Vue {
  events = null

  async asyncData (ctx: Context) {
    const res = await ctx.$axios.$get('/actuator/auditevents')

    return {
      events: res.events,
    }
  }
}
</script>
