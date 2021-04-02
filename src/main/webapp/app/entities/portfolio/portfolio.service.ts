import axios from 'axios';

import {IPortfolio, Portfolio} from '@/shared/model/portfolio.model';
import {PortfolioDTO} from '@/shared/dtos/portfolios-dto.model';


const baseApiUrl = 'api/portfolios';
const baseSearchApiUrl = 'api/_search/portfolios?query=';

export default class PortfolioService {
  public search(query): Promise<Portfolio[]> {
    return new Promise<any>((resolve, reject) => {
      axios.get(`${baseSearchApiUrl}${query}`).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public find(id: number): Promise<IPortfolio> {
    return new Promise<IPortfolio>((resolve, reject) => {
      axios.get(`${baseApiUrl}/${id}`).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public retrieve(): Promise<Portfolio[]> {
    return new Promise<any>((resolve, reject) => {
      axios.get(baseApiUrl).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios.delete(`${baseApiUrl}/${id}`).then(res => {
        resolve(res);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public create(entity: IPortfolio): Promise<IPortfolio> {
    return new Promise<IPortfolio>((resolve, reject) => {
      axios.post(`${baseApiUrl}`, entity).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public update(entity: IPortfolio): Promise<IPortfolio> {
    return new Promise<IPortfolio>((resolve, reject) => {
      axios.put(`${baseApiUrl}`, entity).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public retrieveDtos(): Promise<PortfolioDTO[]> {
    return new Promise<any>((resolve, reject) => {
      axios.get(`${baseApiUrl}/dtos`).then(res => {
        console.log(res.data)
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }
}

