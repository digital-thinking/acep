import {Component, Inject, Vue} from 'vue-property-decorator';

import {IAsset} from '@/shared/model/asset.model';
import AssetService from './asset.service';

@Component
export default class AssetDetails extends Vue {
  public asset: IAsset = {};
  @Inject('assetService') private assetService: () => AssetService;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.assetId) {
        vm.retrieveAsset(to.params.assetId);
      }
    });
  }

  public retrieveAsset(assetId) {
    this.assetService().find(assetId).then((res) => {
      this.asset = res;
    });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
