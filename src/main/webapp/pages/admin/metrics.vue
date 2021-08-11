<template>
  <section>
    <h3 class="app-title">Metrics</h3>

    <select v-model="selectedName" class="form-input" @change="onMetricSelected">
      <option v-for="(metricName, metricNameIndex) in metricNames" :key="metricNameIndex" :value="metricName">{{ metricName }}</option>
    </select>

    <div v-if="selectedMetric != null" class="p-2 bg-white rounded mt-2">
      <div class="text-2xl font-bold">{{ selectedMetric.name }}</div>

      <div class="text-lg py-1 italic">{{ selectedMetric.description }}</div>

      <template v-if="selectedMetric.availableTags.length > 0">
        <hr class="my-4"/>

        <h4 class="text-lg font-bold">Tags</h4>

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
                <select v-model="selectedTags[tag.tag]" class="form-input w-full" @change="onTagSelected(tag.tag)">
                  <option :value="null"></option>
                  <option v-for="(tagValue, tagValueIndex) in tag.values" :key="tagValueIndex" :value="tagValue">{{ tagValue }}</option>
                </select>
              </td>
            </tr>
          </tbody>
        </table>
      </template>

      <template v-if="selectedTagValue != null">

        <hr class="my-4"/>

        <h3 class="text-lg font-bold mb-2"></h3>

        <div v-for="(measure, measureIndex) in selectedTagValue" class="my-2">
          <div class="font-bold text-lg">{{ measure.statistic }}</div>
          <div class="text-md">{{ getMeasure(measure) }}</div>
        </div>
      </template>
    </div>
  </section>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'
import { Context } from '@nuxt/types'

@Component
export default class PageAdminMetrics extends Vue {
  metricNames = []
  selectedName = null
  selectedTags = {}
  selectedTagValue = null
  selectedMetric = null


  async asyncData (ctx: Context) {
    const res = await ctx.$axios.$get('/actuator/metrics')
    return {
      metricNames: res.names,
    }
  }

  async onMetricSelected () {
    this.selectedTags = {}
    this.selectedTagValue = null

    this.selectedMetric = await this.$axios.$get('/actuator/metrics/' + this.selectedName)
    this.selectedTagValue = this.selectedMetric.measurements
  }

  async onTagSelected () {
    if (this.selectedTags != null) {
      const tags = Object.keys(this.selectedTags)
        .filter(tag => this.selectedTags[tag] != null)
        .map(tag => `tag=${ tag }:${ this.selectedTags[tag] }`)
        .join('&')
      const res_metric_tag_value = await this.$axios.$get(`/actuator/metrics/${ this.selectedName }?${ encodeURI(tags) }`)
      this.selectedTagValue = res_metric_tag_value.measurements
    }
  }

  getMeasure (measure) {
    let result

    if ([ 'COUNT', 'ACTIVE_TASKS' ].indexOf(measure.statistic) > -1) {
      return measure.value
    }

    switch (this.selectedMetric.baseUnit) {
      case 'bytes':
        result = this.$utils.formatBytes(measure.value)
        break
      case 'nanoseconds':
      case 'microseconds':
      case 'milliseconds':
      case 'seconds':
        result = (measure.value as number).toFixed(2)
        break
      default:
        result = measure.value
        break
    }

    return this.selectedMetric.baseUnit != null ? `${ result } ${ this.selectedMetric.baseUnit }` : result
  }
}
</script>
