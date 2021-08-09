import { ActionTree, GetterTree, MutationTree } from 'vuex';

export const state = () => ({
  threads: [],
  jvmInfos: [],
})

export type RootState = ReturnType<typeof state>

export const getters: GetterTree<RootState, RootState> = {}

export const mutations: MutationTree<RootState> = {
  SET_THREADS (state, payload) {
    state.threads = payload
  },
  SET_JVM_INFOS (state, payload) {
    state.jvmInfos = payload
  },
}

export const actions: ActionTree<RootState, RootState> = {
  async setThreads ({ commit }) {
    const res = await this.$axios.$get('/admin/threads')

    commit('SET_THREADS', res)
  },
  async setJvmInfos ({ commit }) {
    const res = await this.$axios.$get('/admin/jvm')

    commit('SET_JVM_INFOS', res)
  },
}
