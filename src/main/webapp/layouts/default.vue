<template>
  <section class="bg-gray-50 h-full overflow-auto">
    <app-sidebar :contents="contents"/>
    <div class="ml-0 transition md:ml-60">
      <div class="p-4">
        <nuxt/>
      </div>
    </div>

    <!-- Sidebar Backdrop -->
    <div v-show="sidebar" class="fixed inset-0 z-10 w-screen h-screen bg-black bg-opacity-25 md:hidden" @click="updateSidebar(false)"></div>
  </section>
</template>

<script lang="ts">
import { Action, Component, State, Vue } from 'nuxt-property-decorator'
import AppSidebar from '~/components/app/AppSidebar.vue'
import AppHeader from '~/components/app/AppHeader.vue'

@Component({
  components: {
    AppSidebar, AppHeader,
  },
})
export default class LayoutDefault extends Vue {
  @State('sidebar') sidebar: boolean
  @State('contents') contents: any[]
  @Action('updateSidebar') updateSidebar
  @Action('findContents') findContents

  async fetch () {
    await this.findContents()
  }
}
</script>
