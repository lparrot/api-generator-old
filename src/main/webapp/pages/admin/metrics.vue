<template>
  <section>
    <h3 class="app-title">Metrics</h3>

    <select v-model="selectedName" class="form-input" @change="onMetricSelected">
      <option v-for="(metricName, metricNameIndex) in metricNames" :key="metricNameIndex" :value="metricName">{{ metricName }}</option>
    </select>

    <template v-if="selectedMetric != null">
      <div class="p-2 bg-white rounded mt-2">
        <div class="text-2xl font-bold">{{ selectedMetric.name }}</div>

        <div class="text-lg py-1 italic">{{ selectedMetric.description }}</div>

        <hr/>

        <h4 class="text-lg font-bold mb-2">Tags</h4>

        <table class="w-full">
          <thead>
            <tr>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(tag, tagIndex) in selectedMetric.availableTags" :key="tagIndex">
              <td>
                <span>{{ tag.tag }}</span>
              </td>
              <td>
                <select v-model="selectedTagValue" class="form-input" @change="onTagSelected(tag.tag)">
                  <option v-for="(tagValue, tagValueIndex) in tag.values" :key="tagValueIndex" :value="tagValue">{{ tagValue }}</option>
                </select>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </template>
  </section>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'
import { Context } from '@nuxt/types'

@Component
export default class PageAdminMetrics extends Vue {
  metricNames = []
  selectedName = null
  selectedTagValue = null
  selectedMetric = null


  async asyncData (ctx: Context) {
    const res = await ctx.$axios.$get('/actuator/metrics')
    return {
      metricNames: res.names,
    }
  }

  async onMetricSelected (selected) {
    this.selectedMetric = await this.$axios.$get('/actuator/metrics/' + this.selectedName)
    this.selectedTagValue = null
  }

  async onTagSelected (tag) {
    if (this.selectedTagValue != null) {
      this.selectedTag = await this.$axios.$get(`/actuator/metrics/${ this.selectedName }?tag=${ tag }:${ this.selectedTagValue }`)
    }
  }
}
</script>
