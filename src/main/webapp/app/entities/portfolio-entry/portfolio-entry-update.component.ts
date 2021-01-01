import {Component, Inject, Vue} from 'vue-property-decorator';

import {decimal, numeric, required} from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import {DATE_TIME_LONG_FORMAT} from '@/shared/date/filters';


import PortfolioService from '@/entities/portfolio/portfolio.service';
import {IPortfolio} from '@/shared/model/portfolio.model';


import AssetService from '@/entities/asset/asset.service';
import {IAsset} from '@/shared/model/asset.model';

import {IPortfolioEntry, PortfolioEntry} from '@/shared/model/portfolio-entry.model';
import PortfolioEntryService from './portfolio-entry.service';

const validations: any = {
  portfolioEntry: {
    amount: {
      required,
      numeric,

    },
    price: {
      required,
      decimal,

    },
    bought: {
      required,

    },
    sold: {},
    customName: {},
    group1: {},
    group2: {},
    group3: {},
    group4: {},
  }
};

@Component({
  validations
})
export default class PortfolioEntryUpdate extends Vue {
  public portfolioEntry: IPortfolioEntry = new PortfolioEntry();
  public portfolios: IPortfolio[] = [];
  public assets: IAsset[] = [];
  public isSaving = false;
  public currentLanguage = '';
  @Inject('portfolioEntryService') private portfolioEntryService: () => PortfolioEntryService;
  @Inject('portfolioService') private portfolioService: () => PortfolioService;
  @Inject('assetService') private assetService: () => AssetService;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.portfolioEntryId) {
        vm.retrievePortfolioEntry(to.params.portfolioEntryId);
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
    if (this.portfolioEntry.id) {
      this.portfolioEntryService().update(this.portfolioEntry).then((param) => {
        this.isSaving = false;
        this.$router.go(-1);
        const message = this.$t('acepApp.portfolioEntry.updated', {'param': param.id});
        return this.$root.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'info',
          solid: true,
          autoHideDelay: 5000,
        });
      });
    } else {
      this.portfolioEntryService().create(this.portfolioEntry).then((param) => {
        this.isSaving = false;
        this.$router.go(-1);
        const message = this.$t('acepApp.portfolioEntry.created', {'param': param.id});
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.portfolioEntry[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.portfolioEntry[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.portfolioEntry[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.portfolioEntry[field] = null;
    }
  }

  public retrievePortfolioEntry(portfolioEntryId): void {
    this.portfolioEntryService().find(portfolioEntryId).then((res) => {
      res.bought = new Date(res.bought);
      res.sold = new Date(res.sold);
      this.portfolioEntry = res;
    });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.portfolioService().retrieve().then((res) => {
      this.portfolios = res.data;
    });
    this.assetService().retrieve().then((res) => {
      this.assets = res.data;
    });
  }

}
