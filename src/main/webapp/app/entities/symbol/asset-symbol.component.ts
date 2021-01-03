import {Component, Inject, Vue} from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import {IAssetSymbol} from '@/shared/model/asset-symbol.model.ts';

import AssetSymbolService from './asset-symbol.service';

@Component({
  mixins: [Vue2Filters.mixin]
})
export default class AssetSymbolSearch extends Vue {
  public symbols: IAssetSymbol[] = [];
  public currentSearch = '';
  public isFetching: boolean = false;
  public addId: number = null;
  @Inject('assetSymbolService') private assetSymbolService: () => AssetSymbolService;

  public search(query): void {
    this.isFetching = true;
    if (!query) {
      this.symbols = [];
    }
    this.currentSearch = query;
    this.assetSymbolService().querySymbols(query).then(res => {
      this.symbols = res;
      this.isFetching = false;
    }, err => {
      this.isFetching = false;
    });
    return;
  }


  public addAset(): void {
    console.log("Add");
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }


}
