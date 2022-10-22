![java-version](https://img.shields.io/badge/Java-11-brightgreen?style=flat-square)
[![jitpack-last-release](https://jitpack.io/v/spacious-team/table-wrapper-api.svg?style=flat-square)](
https://jitpack.io/#spacious-team/table-wrapper-api)
[![Unit tests](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2Fspacious-team%2Ftable-wrapper-api%2Fbadge%3Fref%3Ddevelop&style=flat-square&label=Test&logo=none)](
https://github.com/spacious-team/table-wrapper-api/actions/workflows/unit-tests.yml)

#### Оглавление
- [Назначение](#назначение)
- [Пример использования](#пример-использования)
- [Зависимости](#зависимости)

### Назначение
Предоставляет удобный API для доступа к табличным данным из файлов в форматах excel, xml и др.
Пусть на листе excel имеется несколько таблиц.

- Таблица с ценой товаров:

*Таблица товаров*

| Товар  | Цена (опт), руб/кг | Цена розничная, руб/кг |
|--------|--------------------|------------------------|
| Яблоко | 50                 | 90.5                   |
|  Груша | 120                | 180.0                  |

- Таблица с заголовком из 2-х строк:

*Таблица продаж*

| Покупатель |              | Категория  | Объем, |
|------------|--------------|------------|--------|
| Страна     | Компания     | покупателя | кг     |
| Россия     | "Шестерочка" | опт        | 100000 |
| Беларусь   | "Фруктелла"  | опт        | 50000  |
| Итого      |              |            | 150000 |

- Пусть также иногда встречается следующий вариант заголовка предыдущей таблицы (причем заранее не известно какой вариант
встретится в файле):

| Покупатель |          | Категория  | Вес, |
|------------|----------|------------|------|
| Страна     | Компания | покупателя | кг   |

### Пример использования
Для представленного выше примера объявляются описания столбцов:
```java
enum ProductTableHeader implements TableColumnDescription {
    PRODUCT(0),
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
В зависимости от формата исходных данных подготавливаются объекты. Например, для excel файла потребуются
```java
// table wrapper excel impl dependency required 
Workbook book = new XSSFWorkbook(xlsFileinputStream);          // open Excel file
ReportPage reportPage = new ExcelSheet(book.getSheetAt(0));    // select first Excel sheet
```
Используем API для доступа к данным таблиц
```java
// finding row with "таблица товаров" content, parsing next row as header and
// counting next rows as table till empty line
Table productTable = reportPage.create("таблица товаров", null, ProductTableHeader.class);
// finding row with "таблица продаж" content, parsing next 2 rows as header and
// counting next rows as table till row containing "итого" in any cell
Table cellTable = reportPage.create("таблица продаж", "итого",  CellTableHeader.class, 2);

for (TableRow row : productTable) {
    String product = row.getStringCellValueOrDefault(PRICE_TRADE, "Неизвестный товар");
    BigDecimal price = row.getBigDecimalCellValue(PRICE_TRADE);
}

Set<String> countries = cellTable.stream()
    .map(row -> row.getStringCelValueOrDefault(BUYER_COUNTRY, "unknown"))
    .collect(toSet())
```
API предоставляет и другие удобные интерфейсы для работы с таблицами.

### Зависимости
Необходимо подключить репозиторий open source библиотек github [jitpack](https://jitpack.io/#spacious-team/table-wrapper-api),
например для Apache Maven проекта
```xml
<repositories>
    <repository>
        <id>central</id>
        <name>Central Repository</name>
        <url>https://repo.maven.apache.org/maven2</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Далее следует добавить зависимость `table-wrapper-api`
```xml
<dependency>
    <groupId>com.github.spacious-team</groupId>
    <artifactId>table-wrapper-api</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
В качестве версии можно использовать:
- версию [релиза](https://github.com/spacious-team/table-wrapper-api/releases) на github;
- паттерн `<branch>-SNAPSHOT` для сборки зависимости с последнего коммита выбранной ветки;
- короткий десяти значный номер коммита для сборки зависимости с указанного коммита.
 
Вам также потребуется реализация парсера, например
[table-wrapper-excel-impl](https://github.com/spacious-team/table-wrapper-excel-impl) для работы с excel файлами
```xml
<dependency>
    <groupId>com.github.spacious-team</groupId>
    <artifactId>table-wrapper-excel-impl</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```