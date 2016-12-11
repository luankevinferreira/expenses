package luankevinferreira.expenses.dao;

interface Approachable<T> {

    boolean insert(T item) throws Exception;

    boolean update(T item) throws Exception;

    boolean delete(T item) throws Exception;

}
