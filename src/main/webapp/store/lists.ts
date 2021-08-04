import { ActionTree, GetterTree, MutationTree } from 'vuex';

export const state = () => ({
  fieldtypes: null,
})

export type RootState = ReturnType<typeof state>

export const getters: GetterTree<RootState, RootState> = {}

export const mutations: MutationTree<RootState> = {
  SET_FIELD_TYPES (state, payload) {
    state.fieldtypes = payload
  },
}

export const actions: ActionTree<RootState, RootState> = {
  async getFieldTypes ({ commit, state }) {
    if (state.fieldtypes == null) {
      commit('SET_FIELD_TYPES', await this.$axios.$get('/lists/field_types'))
    }
  },
}
