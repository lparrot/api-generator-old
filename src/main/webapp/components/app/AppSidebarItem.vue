<template>
  <nuxt-link :to="to" class="flex items-center px-4 py-3 transition cursor-pointer group hover:bg-gray-800 hover:text-gray-200" exact exact-active-class="bg-gray-800 text-gray-200" @click="updateSidebar(false)">
    <slot>
      <i :class="icon" class="flex-shrink-0 mr-2 text-gray-400 transition group-hover:text-gray-300"></i>
      <span>{{ label }}</span>
    </slot>
  </nuxt-link>
</template>

<script lang="ts">
import { Action, Component, Prop, Vue } from 'nuxt-property-decorator'

@Component
export default class AppSidebarItem extends Vue {
  @Action('updateSidebar') updateSidebar

  @Prop() icon!: string
  @Prop() label!: string
  @Prop() to!: string | any
}
</script>
