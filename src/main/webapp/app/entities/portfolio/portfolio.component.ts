import {Component, Inject, Vue} from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import {IPortfolio} from '@/shared/model/portfolio.model';

import PortfolioService from './portfolio.service';

@Component({
  mixins: [Vue2Filters.mixin]
})
export default class Portfolio extends Vue {
  public currentSearch = '';
  public portfolios: IPortfolio[] = [];
  public isFetching = false;
  @Inject('portfolioService') private portfolioService: () => PortfolioService;
  private removeId: number = null;

  public mounted(): void {
    this.retrieveAllPortfolios();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllPortfolios();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllPortfolios();
  }


  public retrieveAllPortfolios(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.portfolioService().search(this.currentSearch).then(res => {
        this.portfolios = res;
        this.isFetching = false;
      }, err => {
        this.isFetching = false;
      });
      return;
    }
    this.portfolioService().retrieve().then(res => {
      this.portfolios = res.data;
      this.isFetching = false;
    }, err => {
      this.isFetching = false;
    });
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IPortfolio): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removePortfolio(): void {
    this.portfolioService().delete(this.removeId).then(() => {
      const message = this.$t('acepApp.portfolio.deleted', {'param': this.removeId});
      this.$bvToast.toast(message.toString(), {
        toaster: 'b-toaster-top-center',
        title: 'Info',
        variant: 'danger',
        solid: true,
        autoHideDelay: 5000,
      });
      this.removeId = null;
      this.retrieveAllPortfolios();
      this.closeDialog();
    });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
