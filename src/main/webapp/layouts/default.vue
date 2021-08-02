<template>
  <div class="flex h-screen bg-gray-200 font-roboto">
    <Sidebar :contents="contents"/>

    <div class="flex-1 flex flex-col overflow-hidden">
      <Header/>

      <main class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-200">
        <div class="mx-auto px-6 py-8">
          <nuxt/>
        </div>
      </main>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'

@Component
export default class LayoutDefault extends Vue {
  contents: any[] = []

  async fetch () {
    this.contents = await this.$axios.$get('/contents')
  }
}
</script>
