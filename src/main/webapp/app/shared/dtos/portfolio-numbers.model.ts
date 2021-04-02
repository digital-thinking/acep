import {Currency} from "@/shared/model/enumerations/currency.model";

export interface IPortfoliosNumbers {
  performancePercent?: number;
  performanceAmount?: number;
  currency?: Currency;
}

export class PortfoliosNumbers implements IPortfoliosNumbers {
  constructor(
    public performancePercent?: number,
    public performanceAmount?: number,
    public currency?: Currency,
  ) {
  }
}

