<template>
  <nav :class="{ '-translate-x-full' : !sidebar, 'translate-x-0' : sidebar }" class="fixed top-0 left-0 z-20 h-full pb-10 overflow-x-hidden overflow-y-auto transition origin-left transform bg-gray-900 w-60 md:translate-x-0">
    <nav aria-label="Main Navigation" class="text-sm font-medium text-gray-500">

      <nuxt-link class="flex items-center px-4 py-5 text-2xl text-gray-400 hover:text-gray-300" to="/">
        API Generator
      </nuxt-link>

      <div class="flex items-center px-4 py-2">
        <nuxt-link :to="`/content-types`" class="p-btn--success w-full">Add content</nuxt-link>
      </div>

      <app-sidebar-collapse :items="sidebarItems" :open="true" icon="fas fa-cube" label="Collections"></app-sidebar-collapse>

    </nav>
  </nav>
</template>

<script lang="ts">
import { Action, Component, Prop, State, Vue } from 'nuxt-property-decorator'
import AppSidebarCollapse from '~/components/app/AppSidebarCollapse.vue'
import AppSidebarItem from '~/components/app/AppSidebarItem.vue'

@Component({
  components: {
    AppSidebarCollapse, AppSidebarItem,
  },
})
export default class AppSidebar extends Vue {
  @Prop() contents: any[]

  @State('sidebar') sidebar: boolean
  @Action('updateSidebar') updateSidebar
  activeClass: string = 'bg-gray-600 bg-opacity-25 text-gray-100 border-gray-100'
  inactiveClass: string = 'border-gray-900 text-gray-500 hover:bg-gray-600 hover:bg-opacity-25 hover:text-gray-100'

  get sidebarItems () {
    return this.contents.map(content => {
      return { path: `/contents/${ content.id }`, label: content.name }
    })
  }
}
</script>
