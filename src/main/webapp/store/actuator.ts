import { ActionTree, GetterTree, MutationTree } from 'vuex';

export const state = () => ({
  threads: [],
})

export type RootState = ReturnType<typeof state>

export const getters: GetterTree<RootState, RootState> = {}

export const mutations: MutationTree<RootState> = {
  SET_THREADS (state, payload) {
    console.log(payload)
    state.threads = payload
  },
}

export const actions: ActionTree<RootState, RootState> = {
  async setThreads ({ commit }) {
    const res = await this.$axios.$get('/actuator/threaddump')
    commit('SET_THREADS', {
      live: _getThreadFiltered(res.threads, 'RUNNABLE').length,
      blocked: _getThreadFiltered(res.threads, 'BLOCKED').length,
      waiting: _getThreadFiltered(res.threads, 'WAITING').length,
      terminated: _getThreadFiltered(res.threads, 'TERMINATED').length,
      new: _getThreadFiltered(res.threads, 'NEW').length,
    })
  },
}

const _getThreadFiltered = (threads: any[], status: string) => {
  return threads.filter(thread => thread.threadState === status)
}
