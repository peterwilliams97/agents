@echo off
if not "%jade_root%" == "" goto Start
echo Please set the jade_root environment variable to the root of your jade installation
goto Done

:Start
@echo on

java -cp %jade_root%\lib\jade.jar;%jade_root%\lib\jadeTools.jar;%jade_root%\lib\iiop.jar;%jade_root%\lib\commons-codec\commons-codec-1.3.jar;ordering.jar  jade.Boot -agents Seller1:examples.ordering.SupplierAgent(catalog1discount.csv);Seller2:examples.ordering.SupplierAgent(catalog1.csv);Seller3:examples.ordering.SupplierAgent(catalog1.csv);Buyer00:examples.ordering.BuyerAgent(order2.csv);Buyer01:examples.ordering.BuyerAgent(order2.csv);Buyer02:examples.ordering.BuyerAgent(order2.csv);Buyer03:examples.ordering.BuyerAgent(order2.csv);Buyer04:examples.ordering.BuyerAgent(order2.csv);Buyer05:examples.ordering.BuyerAgent(order2.csv);Buyer06:examples.ordering.BuyerAgent(order2.csv);Buyer07:examples.ordering.BuyerAgent(order2.csv);Buyer08:examples.ordering.BuyerAgent(order2.csv);Buyer09:examples.ordering.BuyerAgent(order2.csv);

:Done


