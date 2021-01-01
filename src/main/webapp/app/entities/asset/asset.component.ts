import {Component, Inject, Vue} from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import {IAsset} from '@/shared/model/asset.model';

import AssetService from './asset.service';

@Component({
  mixins: [Vue2Filters.mixin]
})
export default class Asset extends Vue {
  public currentSearch = '';
  public assets: IAsset[] = [];
  public isFetching = false;
  @Inject('assetService') private assetService: () => AssetService;
  private removeId: number = null;

  public mounted(): void {
    this.retrieveAllAssets();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllAssets();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllAssets();
  }


  public retrieveAllAssets(): void {
    this.isFetching = true;

    if (this.currentSearch) {
      this.assetService().search(this.currentSearch).then(res => {
        this.assets = res;
        this.isFetching = false;
      }, err => {
        this.isFetching = false;
      });
      return;
    }
    this.assetService().retrieve().then(res => {
      this.assets = res.data;
      this.isFetching = false;
    }, err => {
      this.isFetching = false;
    });
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IAsset): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeAsset(): void {
    this.assetService().delete(this.removeId).then(() => {
      const message = this.$t('acepApp.asset.deleted', {'param': this.removeId});
      this.$bvToast.toast(message.toString(), {
        toaster: 'b-toaster-top-center',
        title: 'Info',
        variant: 'danger',
        solid: true,
        autoHideDelay: 5000,
      });
      this.removeId = null;
      this.retrieveAllAssets();
      this.closeDialog();
    });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
