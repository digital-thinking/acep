/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon'
import dayjs from 'dayjs';

import {DATE_TIME_FORMAT} from '@/shared/date/filters';
import PortfolioEntryService from '@/entities/portfolio-entry/portfolio-entry.service';
import {PortfolioEntry} from '@/shared/model/portfolio-entry.model';

const error = {
  response: {
    status: null,
    data: {
      type: null
    }
  }
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('PortfolioEntry Service', () => {
    let service: PortfolioEntryService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PortfolioEntryService();
      currentDate = new Date();
      elemDefault = new PortfolioEntry(
        0,
        0,
        0,
        currentDate,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({
          bought: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sold: dayjs(currentDate).format(DATE_TIME_FORMAT),
        }, elemDefault);
        axiosStub.get.resolves({data: returnedFromService});

        return service
          .find(123).then((res) => {
            expect(res).toMatchObject(elemDefault);
          });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service.find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a PortfolioEntry', async () => {
        const returnedFromService = Object.assign({
          id: 0,
          bought: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sold: dayjs(currentDate).format(DATE_TIME_FORMAT),
        }, elemDefault);
        const expected = Object.assign({
          bought: currentDate,
          sold: currentDate,
        }, returnedFromService);

        axiosStub.post.resolves({data: returnedFromService});
        return service.create({}).then((res) => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PortfolioEntry', async () => {
        axiosStub.post.rejects(error);

        return service.create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PortfolioEntry', async () => {
        const returnedFromService = Object.assign({
          amount: 1,
          price: 1,
          bought: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sold: dayjs(currentDate).format(DATE_TIME_FORMAT),
          customName: 'BBBBBB',
          group1: 'BBBBBB',
          group2: 'BBBBBB',
          group3: 'BBBBBB',
          group4: 'BBBBBB',
        }, elemDefault);

        const expected = Object.assign({
          bought: currentDate,
          sold: currentDate,
        }, returnedFromService);
        axiosStub.put.resolves({data: returnedFromService});

        return service.update(expected).then((res) => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PortfolioEntry', async () => {
        axiosStub.put.rejects(error);

        return service.update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PortfolioEntry', async () => {
        const returnedFromService = Object.assign({
          amount: 1,
          price: 1,
          bought: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sold: dayjs(currentDate).format(DATE_TIME_FORMAT),
          customName: 'BBBBBB',
          group1: 'BBBBBB',
          group2: 'BBBBBB',
          group3: 'BBBBBB',
          group4: 'BBBBBB',
        }, elemDefault);
        const expected = Object.assign({
          bought: currentDate,
          sold: currentDate,
        }, returnedFromService);
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then((res) => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PortfolioEntry', async () => {
        axiosStub.get.rejects(error);

        return service.retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PortfolioEntry', async () => {
        axiosStub.delete.resolves({ok: true});
        return service.delete(123).then((res) => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PortfolioEntry', async () => {
        axiosStub.delete.rejects(error);

        return service.delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
