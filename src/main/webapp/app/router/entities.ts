import {Authority} from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Portfolio = () => import('@/entities/portfolio/portfolio.vue');
// prettier-ignore
const PortfolioUpdate = () => import('@/entities/portfolio/portfolio-update.vue');
// prettier-ignore
const PortfolioDetails = () => import('@/entities/portfolio/portfolio-details.vue');
// prettier-ignore
const PortfolioEntry = () => import('@/entities/portfolio-entry/portfolio-entry.vue');
// prettier-ignore
const PortfolioEntryUpdate = () => import('@/entities/portfolio-entry/portfolio-entry-update.vue');
// prettier-ignore
const PortfolioEntryDetails = () => import('@/entities/portfolio-entry/portfolio-entry-details.vue');
// prettier-ignore
const Asset = () => import('@/entities/asset/asset.vue');
// prettier-ignore
const AssetUpdate = () => import('@/entities/asset/asset-update.vue');
// prettier-ignore
const AssetDetails = () => import('@/entities/asset/asset-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/portfolio',
    name: 'Portfolio',
    component: Portfolio,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/portfolio/new',
    name: 'PortfolioCreate',
    component: PortfolioUpdate,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/portfolio/:portfolioId/edit',
    name: 'PortfolioEdit',
    component: PortfolioUpdate,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/portfolio/:portfolioId/view',
    name: 'PortfolioView',
    component: PortfolioDetails,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/portfolio-entry',
    name: 'PortfolioEntry',
    component: PortfolioEntry,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/portfolio-entry/new',
    name: 'PortfolioEntryCreate',
    component: PortfolioEntryUpdate,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/portfolio-entry/:portfolioEntryId/edit',
    name: 'PortfolioEntryEdit',
    component: PortfolioEntryUpdate,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/portfolio-entry/:portfolioEntryId/view',
    name: 'PortfolioEntryView',
    component: PortfolioEntryDetails,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/asset',
    name: 'Asset',
    component: Asset,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/asset/new',
    name: 'AssetCreate',
    component: AssetUpdate,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/asset/:assetId/edit',
    name: 'AssetEdit',
    component: AssetUpdate,
    meta: {authorities: [Authority.USER]}
  },
  {
    path: '/asset/:assetId/view',
    name: 'AssetView',
    component: AssetDetails,
    meta: {authorities: [Authority.USER]}
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
