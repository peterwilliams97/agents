@echo off
if not "%jade_root%" == "" goto Start
echo Please set the jade_root environment variable to the root of your jade installation
goto Done

:Start
@echo on
java -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;ordering.jar  jade.Boot -agents Buyer1:examples.ordering.BuyerAgent(order2.csv);Seller1:examples.ordering.SupplierAgent(catalog1.csv)

:Done
