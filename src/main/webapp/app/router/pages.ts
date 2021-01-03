/* tslint:disable */
// prettier-ignore

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

import {Authority} from "@/shared/security/authority";

const AssetSymbol = () => import('@/entities/symbol/asset-symbol.vue');
// prettier-ignore

export default [
  {
    path: '/symbol/search',
    name: 'SymbolSearch',
    component: AssetSymbol,
    meta: {authorities: [Authority.USER]}
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
]
