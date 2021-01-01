import {Component, Inject, Vue} from 'vue-property-decorator';

import {required} from 'vuelidate/lib/validators';


import PortfolioEntryService from '@/entities/portfolio-entry/portfolio-entry.service';
import {IPortfolioEntry} from '@/shared/model/portfolio-entry.model';

import {Asset, IAsset} from '@/shared/model/asset.model';
import AssetService from './asset.service';

const validations: any = {
  asset: {
    name: {
      required,

    },
    currency: {
      required,

    },
    assetType: {
      required,

    },
    symbol: {
      required,

    },
    source: {
      required,

    },
  }
};

@Component({
  validations
})
export default class AssetUpdate extends Vue {
  public asset: IAsset = new Asset();
  public portfolioEntries: IPortfolioEntry[] = [];
  public isSaving = false;
  public currentLanguage = '';
  @Inject('assetService') private assetService: () => AssetService;
  @Inject('portfolioEntryService') private portfolioEntryService: () => PortfolioEntryService;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.assetId) {
        vm.retrieveAsset(to.params.assetId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.asset.id) {
      this.assetService().update(this.asset).then((param) => {
        this.isSaving = false;
        this.$router.go(-1);
        const message = this.$t('acepApp.asset.updated', {'param': param.id});
        return this.$root.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'info',
          solid: true,
          autoHideDelay: 5000,
        });
      });
    } else {
      this.assetService().create(this.asset).then((param) => {
        this.isSaving = false;
        this.$router.go(-1);
        const message = this.$t('acepApp.asset.created', {'param': param.id});
        this.$root.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Success',
          variant: 'success',
          solid: true,
          autoHideDelay: 5000,
        });
      });
    }
  }


  public retrieveAsset(assetId): void {
    this.assetService().find(assetId).then((res) => {
      this.asset = res;
    });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.portfolioEntryService().retrieve().then((res) => {
      this.portfolioEntries = res.data;
    });
  }

}
