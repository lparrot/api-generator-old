<template>
  <div class="min-w-max">
    <section class="flex justify-between bg-white rounded-lg border border-gray-200 px-10 py-3 text-gray-700 font-montserrat">
      <ul class="flex items-center">
        <li class="pr-6">
          <button :disabled="!hasPrev" class="flex items-center justify-center hover:bg-gray-200 rounded-md transform rotate-45 h-6 w-6 disabled:cursor-not-allowed disabled:opacity-50" @click.prevent="changePage(prevPage)">
              <span class="transform -rotate-45">
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path d="M15 19l-7-7 7-7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
                </svg>
              </span>
          </button>
        </li>
        <li v-for="page in pages" class="pr-6">
          <button :class="{'bg-secondary-400 text-white': paginationSync.page === page}" class="flex hover:bg-primary-200 rounded-md transform rotate-45 h-6 w-6 items-center justify-center" @click.prevent="changePage(page)">
            <span class="transform -rotate-45">{{ page }}</span>
          </button>
        </li>
        <li class="pr-6">
          <button :disabled="!hasNext" class="flex items-center justify-center hover:bg-primary-200 rounded-md transform rotate-45 h-6 w-6 disabled:cursor-not-allowed disabled:opacity-50" @click.prevent="changePage(nextPage)">
              <span class="transform -rotate-45">
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path d="M9 5l7 7-7 7" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"/>
                </svg>
              </span>
          </button>
        </li>
      </ul>

      <form class="flex items-center" @submit.prevent="changePage(input)">
        <div class="pr-2 text-primary-400 font-medium">
          <span id="text-before-input">
            {{ textBeforeInput }}
          </span>
        </div>
        <div class="flex items-center gap-2">
          <div class="w-14 rounded-md border border-secondary-400 px-1 py-1">
            <input v-model.number="input" class="w-12 focus:outline-none px-2" type="text">
          </div>
          <div class="rounded-md border border-secondary-400 px-1 py-1">
            <select v-model.number="paginationSync.size" class="focus:outline-none px-2" @input="changePage(1)">
              <option :value="5">5</option>
              <option :value="15">15</option>
              <option :value="50">50</option>
              <option :value="100">100</option>
            </select>
          </div>
        </div>
      </form>
    </section>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, PropSync, Vue } from 'nuxt-property-decorator'
import range from 'lodash.range';

@Component
export default class Pagination extends Vue {
  @Prop({ default: 5 }) pageRange!: number

  @Prop({ type: String, default: 'Go to page' }) textBeforeInput!: string
  @Prop({ type: String, default: 'Go' }) textAfterInput!: string

  @PropSync('pagination', {
    default: () => {
      return { size: 15, total: 0, page: 1 }
    },
  }) paginationSync: { size: number, total: number, page: number }

  input = ''

  get pages () {
    const from1 = this.paginationSync.page - Math.round(this.pageRange / 2) + 1;
    const from2 = (this.totalPages) + 1 - (this.pageRange);
    const from = Math.max(Math.min(from1, from2), 1);

    const to = Math.min(from + this.pageRange - 1, this.totalPages);

    return range(from, to + 1).map((page) => {
      if (page === from && from > 1) {
        return page;
      }

      if (page === to && to < this.totalPages) {
        return page;
      }

      return page;
    });
  }

  get totalPages () {
    return Math.ceil(this.paginationSync.total / this.paginationSync.size)
  }

  get nextPage () {
    return this.paginationSync.page + 1
  }

  get prevPage () {
    return this.paginationSync.page - 1
  }

  get hasFirst () {
    return this.paginationSync.page !== 1
  }

  get hasLast () {
    return this.paginationSync.page < this.totalPages
  }

  get hasPrev () {
    return this.paginationSync.page > 1
  }

  get hasNext () {
    return this.paginationSync.page < this.totalPages
  }

  @Emit('page-changed')
  changePage (page) {
    if (page <= 0) {
      this.paginationSync.page = 1
    } else if (page > this.totalPages) {
      this.paginationSync.page = this.totalPages
    } else {
      this.paginationSync.page = page
    }

    this.input = ''

    return this.paginationSync
  }
}
</script>

<style lang="scss" scoped>
.pagination-item {
  @apply flex items-center justify-center cursor-pointer border border-primary-300 w-8 h-8 rounded-full hover:bg-secondary-300 hover:text-white;

  &.active {
    @apply bg-secondary-400 text-white;
  }
}
</style>
