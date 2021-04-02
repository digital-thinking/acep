export interface IStockPrice {
  date?: Date;
  ask?: number;
  bid?: number;
  low?: number;
  high?: number;
}

export class StockPrice implements IStockPrice {
  constructor(
    public date?: Date,
    public ask?: number,
    public bid?: number,
    public low?: number,
    public high?: number
  ) {
  }
}
