import {Component, Inject, Vue} from 'vue-property-decorator';

import {maxLength, minLength, required} from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import {DATE_TIME_LONG_FORMAT} from '@/shared/date/filters';


import PortfolioEntryService from '@/entities/portfolio-entry/portfolio-entry.service';
import {IPortfolioEntry} from '@/shared/model/portfolio-entry.model';

import {IPortfolio, Portfolio} from '@/shared/model/portfolio.model';
import PortfolioService from './portfolio.service';

const validations: any = {
  portfolio: {
    name: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(255),

    },
    created: {},
  }
};

@Component({
  validations
})
export default class PortfolioUpdate extends Vue {
  public portfolio: IPortfolio = new Portfolio();
  public portfolioEntries: IPortfolioEntry[] = [];
  public isSaving = false;
  public currentLanguage = '';
  @Inject('portfolioService') private portfolioService: () => PortfolioService;
  @Inject('portfolioEntryService') private portfolioEntryService: () => PortfolioEntryService;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.portfolioId) {
        vm.retrievePortfolio(to.params.portfolioId);
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
    if (this.portfolio.id) {
      this.portfolioService().update(this.portfolio).then((param) => {
        this.isSaving = false;
        this.$router.go(-1);
        const message = this.$t('acepApp.portfolio.updated', {'param': param.id});
        return this.$root.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'info',
          solid: true,
          autoHideDelay: 5000,
        });
      });
    } else {
      this.portfolioService().create(this.portfolio).then((param) => {
        this.isSaving = false;
        this.$router.go(-1);
        const message = this.$t('acepApp.portfolio.created', {'param': param.id});
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
      this.portfolio[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.portfolio[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.portfolio[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.portfolio[field] = null;
    }
  }

  public retrievePortfolio(portfolioId): void {
    this.portfolioService().find(portfolioId).then((res) => {
      res.created = new Date(res.created);
      this.portfolio = res;
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
