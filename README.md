![java-version](https://img.shields.io/badge/Java-11-brightgreen?style=flat-square)
![jitpack-last-release](https://jitpack.io/v/spacious-team/table-wrapper-api.svg?style=flat-square)

#### Оглавление
- [Назначение](#назначение)
- [Пример использования](#пример-использования)
- [Зависимости](#зависимости)

### Назначение
Предоставит удобный API для доступа к табличным данным из файлов в форматах excel, xml и др.
Представим, что на листе excel имеется несколько таблиц, одна под другой.

Простая таблица с ценой товаров:

*Таблица товаров*

Товар | Цена (опт), руб/кг | Цена розничная, руб/кг
------|--------------------|----------------
Яблоко| 50                 | 90.5
Груша | 120                | 180.0

Таблица с мультистрочным заголовком:

*Таблица продаж*

Покупатель |                |  Категория  | Объем, 
-----------|----------------|-------------|--------
Страна     | Компания       |  покупателя | кг 
Россия     | "Шестерочка"   | опт         | 100000 
Беларусь   | "Фруктелла"    | опт         |  50000
Итого      |                |             | 150000


Пусть также встречается вариант следующий заголовка предыдущей таблицы (причем заранее не известно какой вариант
встретится в файле):

Покупатель |                |  Категория   | Вес, 
-----------|----------------|--------------|--------
Страна     | Компания       |  покупателя  | кг 

### Пример использования
Для представленного выше примера объявляются описания столбцов:
```java
enum ProductTableHeader implements TableColumnDescription {
    PRODUCT(1),
    PRICE_TRADE("цена", "опт"),
    PRICE("цена", "розничная");

    private final TableColumn column;

    ProductTableHeader(int columnIndex) {
        this.column = ConstantPositionTableColumn.of(columnIndex);
    }

    ProductTableHeader(String... words) {
        this.column = TableColumnImpl.of(words);
    }

    public TableColumn getColumn() {
        return column;
    }   
}

enum CellTableHeader implements TableColumnDescription {
    BUYER_COUNTRY(MultiLineTableColumn.of("покупатель", "страна")),
    BUYER_COMPANY(MultiLineTableColumn.of("покупатель", "компания")),
    TYPE(MultiLineTableColumn.of("категория", "покупателя")),
    VOLUME(AnyOfTableColumn.of(
                           MultiLineTableColumn.of("объем", "кг"),
                           MultiLineTableColumn.of("вес", "кг")));

    private final TableColumn column;

    CellTableHeader(TableColumn column) {
        this.column = column;
    }

    public TableColumn getColumn() {
        return column;
    }  
}
```
Подготавливаем объекты, зависящие от формата файла. Например для excel файла
```java
// table wrapper excel impl dependency required 
Workbook book = new XSSFWorkbook(xlsFileinputStream);          // opening excel file
ReportPage reportPage = new ExcelSheet(book.getSheetAt(0));    // selecting excel sheet
TableFactory tableFactory = new ExcelTableFactory(reportPage);
```
Используем API для доступа к данным таблиц
```java
// finding row with "таблица товаров" content, parsing next row as header and
// counting next rows as table till empty line
Table productTable = tableFactory.create(reportTable, "таблица товаров", null, ProductTableHeader.class);
// finding row with "таблица продаж" content, parsing next 2 rows as header and
// counting next rows as table till row containing "итого" in any cell
Table cellTable = tableFactory.create(reportTable, "таблица продаж", "итого",  CellTableHeader.class, 2);

for (TableRow row : productTable) {
    String product = table.getStringCellValueOrDefault(row, PRICE_TRADE, "Неизвестный товар");
    BigDecimal price = table.getCurrencyCellValue(row, PRICE_TRADE);
}
```
API предоставляет и другие удобные интерфейсы для работы с таблицами.

### Зависимости
Рассмотрим на примере Apache Maven проекта. Подключаем репозиторий библиотек, размещаемых на github
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
и подключаем зависимость `table-wrapper-api`
```xml
<dependency>
    <groupId>com.github.spacious-team</groupId>
    <artifactId>table-wrapper-api</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
Вам также подтребуется подключить зависимой одной из реализаций парсера, например excel
```xml
<dependency>
    <groupId>com.github.spacious-team</groupId>
    <artifactId>table-wrapper-excel-impl</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```