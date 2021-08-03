<template>
  <div>
    <div class="flex items-center justify-between px-4 py-3 transition cursor-pointer group hover:bg-gray-800 hover:text-gray-200" role="button" @click="toggleOpen">
      <div class="flex items-center">
        <slot name="menu">
          <i v-if="icon" :class="icon" class="flex-shrink-0 mr-2 text-gray-400 transition group-hover:text-gray-300"></i>
          <span v-if="label">{{ label }}</span>
        </slot>
      </div>
      <svg :class="{ 'rotate-90': d_open }" class="flex-shrink-0 w-4 h-4 ml-2 transition transform" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
        <path clip-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" fill-rule="evenodd"/>
      </svg>
    </div>
    <div v-show="d_open" class="mb-4" @click="updateSidebar(false)">
      <slot name="items">
        <nuxt-link v-for="(item, itemIndex) in items" :key="itemIndex" :to="item.path" class="flex items-center py-2 pl-12 pr-4 transition cursor-pointer hover:bg-gray-800 hover:text-gray-200" exact exact-active-class="bg-gray-800 text-gray-200">{{ item.label }}</nuxt-link>
      </slot>
    </div>
  </div>
</template>

<script lang="ts">
import { Action, Component, Prop, Vue } from 'nuxt-property-decorator'

@Component
export default class AppSidebarCollapse extends Vue {
  @Action('updateSidebar') updateSidebar

  @Prop() icon!: string
  @Prop() items!: any[]
  @Prop() label!: string
  @Prop({ default: false }) open!: boolean

  d_open = this.open

  toggleOpen () {
    this.d_open = !this.d_open
  }
}
</script>
