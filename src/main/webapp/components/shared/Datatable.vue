<template>
  <div class="container mx-auto">
    <div class="py-8">
      <div class="py-4 overflow-x-auto">
        <div class="flex justify-center mb-2">
          <pagination :pagination.sync="paginationSync" @page-changed="onChangePage"/>
        </div>

        <div class="inline-block min-w-full shadow rounded-lg overflow-hidden">
          <table :class="d_classes.table">
            <thead :class="d_classes.head">
              <tr :class="d_classes.headTr">
                <th v-for="field in fields" :key="field.key" :class="d_classes.headTh">
                  <slot :field="field" :name="`head(${field.key})`">
                    <span>{{ field.label }}</span>
                  </slot>
                </th>
              </tr>
            </thead>
            <tbody :class="d_classes.body">
              <template v-if="items.length">
                <tr v-for="(item, itemIndex) in items" :key="itemIndex" :class="d_classes.bodyTr">
                  <td v-for="field in fields" :key="item + field.key" :class="[typeof d_classes.bodyTd === 'function' ? d_classes.bodyTd(item[field.key]) : d_classes.bodyTd, field.class]">
                    <slot :item="item" :name="`cell(${field.key})`" :value="item[field.key]">
                      <span>{{ item[field.key] }}</span>
                    </slot>
                  </td>
                </tr>
              </template>
              <tr v-else :class="d_classes.bodyTr">
                <td :class="d_classes.bodyTd" colspan="100%">
                  <p>No items</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="flex justify-center mt-2">
          <pagination :pagination.sync="paginationSync" @page-changed="onChangePage"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, PropSync, Vue } from 'nuxt-property-decorator'
import Pagination from '~/components/shared/Pagination.vue'

export interface DatatableClasses {
  table?: string
  head?: string
  headTr?: string
  headTh?: string
  th?: string
  body?: string
  bodyTr?: string
  bodyTd?: ((val: any) => string) | string
}

export interface DatatableField {
  key: string
  label?: string
  class?: string | string[]
}

@Component({
  components: {
    Pagination,
  },
})
export default class Datatable extends Vue {
  @Prop({ type: Object }) classes!: DatatableClasses
  @Prop({ type: Array, required: true }) fields!: DatatableField[]
  @Prop({ type: Array, required: true }) items!: any[]
  @Prop({ type: Boolean, default: false }) withActions!: boolean
  @PropSync('pagination') paginationSync

  d_classes = this.classes

  fetch () {
    this.d_classes = {
      table: 'min-w-full leading-normal',
      headTh: 'px-5 py-3 bg-white  border-b border-gray-200 text-gray-800 text-left text-sm uppercase font-normal',
      bodyTd: 'px-5 py-5 border-b border-gray-200 bg-white text-sm text-left',
    }

    if (this.withActions) {
      this.fields.push({ key: 'actions', label: '', class: 'w-20' })
    }
  }

  @Emit('context-changed')
  async onChangePage (pagination) {
    return pagination
  }
}
</script>
