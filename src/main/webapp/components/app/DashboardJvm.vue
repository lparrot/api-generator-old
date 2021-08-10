<template>
  <canvas id="chart-jvm"/>
</template>

<script lang="ts">
import { Action, Component, State, Vue, Watch } from 'nuxt-property-decorator'
import { Chart } from 'chart.js'

@Component
export default class DashboardJvm extends Vue {

  @State(state => state.actuator.jvmInfos) jvmInfos
  @Action('actuator/setJvmInfos') setJvmInfos

  chart: Chart

  async mounted () {
    await this.setJvmInfos()
    this.updateChart()
  }

  updateChart () {
    const canvas = document.getElementById('chart-jvm') as HTMLCanvasElement
    const data = {
      labels: this.jvmInfos.map(jvm => jvm.timestamp),
      datasets: [
        {
          label: 'USED (MB)',
          data: this.jvmInfos.map(jvm => jvm.used / 1000000),
          fill: true,
          borderColor: '#717eec',
          backgroundColor: '#717eec',
          tension: 0.4,
          pointRadius: 0,
        },
        {
          label: 'SIZE (MB)',
          data: this.jvmInfos.map(jvm => jvm.size / 1000000),
          fill: true,
          borderColor: '#ffeb3b',
          backgroundColor: '#ffeb3b',
          tension: 0.4,
          pointRadius: 0,
        },
      ],
    }

    if (this.chart == null) {
      this.chart = new Chart(canvas, {
        type: 'line',
        data,
        options: {
          responsive: true,
          interaction: {
            mode: 'index',
            intersect: false,
          },
          scales: {
            yAxes: {
              beginAtZero: true,
              grid: {
                display: false,
              },
            },
            xAxes: {
              display: false,
              grid: {
                display: false,
              },
            },
          },
        },
      })
    } else {
      this.chart.data = data
    }

    this.chart.update()
  }

  @Watch('jvmInfos', { immediate: false, deep: true })
  watchJvmInfos () {
    this.updateChart()
  }

}
</script>
