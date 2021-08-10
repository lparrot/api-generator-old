<template>
  <canvas id="chart-thread"/>
</template>

<script lang="ts">
import { Action, Component, State, Vue, Watch } from 'nuxt-property-decorator'
import { Chart } from 'chart.js'

@Component
export default class DashboardThread extends Vue {

  @State(state => state.actuator.threads) threads
  @Action('actuator/setThreads') setThreads

  chart: Chart

  async mounted () {
    await this.setThreads()
    this.updateChart()
  }

  updateChart () {
    const canvas = document.getElementById('chart-thread') as HTMLCanvasElement
    const data = {
      labels: this.threads.map(thread => thread.timestamp),
      datasets: [
        {
          label: 'WAITING',
          data: this.threads.map(thread => thread.deamon),
          fill: true,
          borderColor: '#717eec',
          backgroundColor: '#717eec',
          tension: 0.4,
          pointRadius: 0,
        },
        {
          label: 'LIVE',
          data: this.threads.map(thread => thread.live),
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
          animation: {
            duration: 0,
          },
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

  @Watch('threads', { immediate: false, deep: true })
  watchThreads () {
    this.updateChart()
  }

}
</script>
