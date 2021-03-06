import { ActionTree, GetterTree, MutationTree } from 'vuex';

export const state = () => ({
  fieldtypes: null,
  fieldRelationTypes: null,
  fieldShowTypes: null,
})

export type RootState = ReturnType<typeof state>

export const getters: GetterTree<RootState, RootState> = {}

export const mutations: MutationTree<RootState> = {
  SET_FIELD_TYPES (state, payload) {
    state.fieldtypes = payload
  },

  SET_FIELD_RELATION_TYPES (state, payload) {
    state.fieldRelationTypes = payload
  },

  SET_FIELD_SHOW_TYPES (state, payload) {
    state.fieldShowTypes = payload
  },
}

export const actions: ActionTree<RootState, RootState> = {
  async findFieldTypes ({ commit, state }) {
    if (state.fieldtypes == null) {
      commit('SET_FIELD_TYPES', await this.$axios.$get('/lists/field_types'))
    }
  },

  async findFieldRelationTypes ({ commit, state }) {
    if (state.fieldRelationTypes == null) {
      commit('SET_FIELD_RELATION_TYPES', await this.$axios.$get('/lists/field_relation_types'))
    }
  },

  async findFieldShowTypes ({ commit, state }) {
    if (state.fieldShowTypes == null) {
      commit('SET_FIELD_SHOW_TYPES', await this.$axios.$get('/lists/field_show_types'))
    }
  },
}
