import Component from 'vue-class-component';
import {Inject, Vue} from 'vue-property-decorator';
import LoginService from '@/account/login.service';
import ActivateService from './activate.service';

@Component
export default class Activate extends Vue {
  success = false;
  error = false;
  @Inject('activateService')
  private activateService: () => ActivateService;
  @Inject('loginService')
  private loginService: () => LoginService;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.query.key) {
        vm.init(to.query.key);
      }
    });
  }

  public init(key: string): void {
    this.activateService()
      .activateAccount(key)
      .then(
        res => {
          this.success = true;
          this.error = false;
        },
        err => {
          this.error = true;
          this.success = false;
        }
      );
  }

  public openLogin(): void {
    this.loginService().openLogin((<any>this).$root);
  }
}
