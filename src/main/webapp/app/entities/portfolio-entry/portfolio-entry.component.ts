import {Component, Inject, Vue} from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import {IPortfolioEntry} from '@/shared/model/portfolio-entry.model';

import PortfolioEntryService from './portfolio-entry.service';

@Component({
  mixins: [Vue2Filters.mixin]
})
export default class PortfolioEntry extends Vue {
  public currentSearch = '';
  public portfolioEntries: IPortfolioEntry[] = [];
  public isFetching = false;
  @Inject('portfolioEntryService') private portfolioEntryService: () => PortfolioEntryService;
  private removeId: number = null;

  public mounted(): void {
    this.retrieveAllPortfolioEntrys();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllPortfolioEntrys();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllPortfolioEntrys();
  }


  public retrieveAllPortfolioEntrys(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.portfolioEntryService().search(this.currentSearch).then(res => {
        this.portfolioEntries = res;
        this.isFetching = false;
      }, err => {
        this.isFetching = false;
      });
      return;
    }
    this.portfolioEntryService().retrieve().then(res => {
      this.portfolioEntries = res.data;
      this.isFetching = false;
    }, err => {
      this.isFetching = false;
    });
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IPortfolioEntry): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removePortfolioEntry(): void {
    this.portfolioEntryService().delete(this.removeId).then(() => {
      const message = this.$t('acepApp.portfolioEntry.deleted', {'param': this.removeId});
      this.$bvToast.toast(message.toString(), {
        toaster: 'b-toaster-top-center',
        title: 'Info',
        variant: 'danger',
        solid: true,
        autoHideDelay: 5000,
      });
      this.removeId = null;
      this.retrieveAllPortfolioEntrys();
      this.closeDialog();
    });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
