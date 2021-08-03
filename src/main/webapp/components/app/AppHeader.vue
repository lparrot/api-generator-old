<template>
  <header class="flex items-center justify-between w-full px-4 h-14">
    <button class="block btn btn-light-secondary md:hidden" @click="updateSidebar(true)">
      <span class="sr-only">Menu</span>
      <i class="fas fa-bars"></i>
    </button>
    <div class="hidden form-icon md:block w-96"></div>
    <div class="flex items-center">
      <nuxt-link class="flex text-gray-500" to="/notifications">
        <i class="fas fa-bell"></i>
      </nuxt-link>
    </div>
  </header>
</template>

<script lang="ts">
import { Action, Component, Vue } from 'nuxt-property-decorator'

@Component
export default class AppHeader extends Vue {
  @Action('updateSidebar') updateSidebar
}
</script>
